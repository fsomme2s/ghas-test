// @ts-nocheck

import { generateApi } from 'swagger-typescript-api';
import axios from 'axios';
import fs from 'fs';
import fsp from 'fs/promises';
import path from 'path';
import Handlebars from 'handlebars';
import { OpenAPIV3 } from 'openapi-types';

///////////////////////////////////////////////////////////

const urls = {
  api: 'http://localhost:8080/api/_openapi/v3/api-docs',
};
const targetDir = `${process.cwd()}/src/app/api`

// adds additional async methods
const withAsync: boolean = true

///////////////////////////////////////////////////////////

Handlebars.registerHelper('skima-switch', function (value, options) {
  this.switch_value = value;
  return options.fn(this);
});

Handlebars.registerHelper('skima-case', function (value, options) {
  if (value == this.switch_value) {
    return options.fn(this);
  }
});

Handlebars.registerHelper('skima-default', function (options) {
  return options.fn(this);
});

///////////////////////////////////////////////////////////

// clean target dir:
fs.rm(targetDir + '/model', {recursive: true, force: true}, (err) => {
  if (err) {
    console.error(`Error removing directory: ${err}`);
  } else {
    console.log(`Directory ${targetDir} removed successfully`);
  }
});
fs.rm(targetDir + '/services', {recursive: true, force: true}, (err) => {
  if (err) {
    console.error(`Error removing directory: ${err}`);
  } else {
    console.log(`Directory ${targetDir} removed successfully`);
  }
});


const templatePath = path.join(process.cwd(), 'generate-types/api-service.template.hbs');
const templateSource = fs.readFileSync(templatePath, 'utf8');
const hbsTemplate = Handlebars.compile(templateSource);

Object.keys(urls).forEach(async (key) => {
  const url = `${urls[key]}`;

  try {
    // Fetch the OpenAPI spec
    const {data: openApiSpec} = await axios.get<OpenAPIV3.Document>(url);

    // Generate the API types
    await generateApi({
      name: `${key}.types.ts`,
      output: `${targetDir}/model`,
      url,
      generateClient: false,
      extractRequestParams: true,
      generateUnionEnums: true,
      hooks: {
        onParseSchema: (_originalSchema, parsedSchema) => {
          if (isArray(parsedSchema.content)) {
            parsedSchema.content.forEach((content: { field: string; isNullable: boolean }) => {
              if (content.field && !content.isNullable) {
                content.field = content.field.replace('?', '');
              }
            });
          }

          return cloneDeep(parsedSchema);
        },
      },
    });

    // Create Api Class in a non-bloated non-maniac way (in contrast to all other existing generators - wtf?)
    const operationsByTag: Map<string, Operation[]> = getOperationsByTag(openApiSpec);
    const serviceOutputDir = `${targetDir}/services`;

    if (!fs.existsSync(serviceOutputDir)) {
      fs.mkdirSync(serviceOutputDir, {recursive: true});
    }

    for (let [tagName, operations] of operationsByTag.entries()) {
      // extract additionalinfos from openapi spec:
      const apiName = convertToPascalCase(tagName);
      const apiDoc = openApiSpec.tags.filter(tagDef => tagDef.name === tagName)[0]?.description || '';

      let allUsedTypes = deduplicate([
        ...operations.map(op => op.returnType),
        ...operations.map(op => op.requestBodyType)
      ])
        .filter(t => t != null && !['void', 'string', 'number', 'Date'].includes(t))
        .join(', ');

      // Create api-file from handlebar template:
      const filePath = path.join(serviceOutputDir, `${tagName}.service.ts`);
      if (!(await fsp.stat(filePath).catch(() => false))) {
        const fileContent = hbsTemplate({
          withAsync,
          apiName,
          apiDoc,
          operations,
          allUsedTypes,
        });
        await fsp.writeFile(filePath, fileContent);
        console.log(`Created service file: ${filePath}`);
      }
    }

  } catch (error) {
    console.log(error);
  }
});

interface Operation {
  withAsync: boolean; // if this op should have a second version with async instead of Observable (currently set globally)
  path: string;
  operationId: string;
  method: string;
  returnType: string;
  isReturnTypeArray: boolean;
  isTextResponse: boolean; // true if this is text/plain response
  requestBodyType: string;
  methodParams: MethodParam[];
  hasQueryParams: boolean;
  hasOptionsObject: boolean; // if hasQueryParams or isTextResponse
}

interface MethodParam {
  paramName: string;
  paramType: string; // class like string, a Dto etc
  paramOpt: boolean;
}

function getOperationsByTag(openApiSpec: OpenAPIV3.Document) {
  const operationsByTag: Map<string, Operation[]> = new Map()

  if (openApiSpec.paths) {
    Object.entries(openApiSpec.paths).forEach(([path, methods]) => {
      Object.entries(methods).forEach(([method, operation]) => {
        if (operation.tags) {
          operation.tags.forEach((tag) => {
            if (!operationsByTag.has(tag)) {
              operationsByTag.set(tag, [])
            }

            // Response Type:
            let [statusCode, mandatoryResponse] = Object.entries(operation.responses)
              .filter(([statusCode, rsp]) => statusCode >= 200 && statusCode <= 299)[0]

            let returnType: string;
            let isReturnTypeArray: boolean = false;
            if (!(mandatoryResponse == null || mandatoryResponse.content == null)) {
              let rspSchema = Object.values(mandatoryResponse.content)[0].schema;
              if (rspSchema['$ref']) {
                returnType = rspSchema['$ref'].split('/').pop()
              } else if (rspSchema['type']) {
                let type = rspSchema['type'];

                // case array:
                if (type === 'array') {
                  // todo this should be some kind of recursive call

                  isReturnTypeArray = true;
                  let itemRspSchema = rspSchema.items;
                  if (itemRspSchema['$ref']) {
                    returnType = itemRspSchema['$ref'].split('/').pop()
                  } else if (rspSchema['type']) {
                    returnType = itemRspSchema['$type']
                  } else {
                    returnType = 'any'
                  }

                  // end array
                } else {
                  returnType = type
                }
              } else {
                console.error('Unkown response schema type: ', rspSchema)
                returnType = 'void'
              }
            } else {
              returnType = 'void'
            }

            // Request Body:
            let hasRequestBody = method === 'post' || method === 'pust'
            let requestBodyType: string = null;
            let requestBodyVarName: string = null;

            if (hasRequestBody) {
              if (operation.requestBody != null) {
                let reqSchema = requestBodyType = Object.values(operation.requestBody.content)[0].schema;
                requestBodyType = reqSchema['$ref'].split('/').pop()
                requestBodyVarName = requestBodyType.charAt(0).toLowerCase() + requestBodyType.slice(1)
              } else {
                requestBodyVarName = 'null'
              }
            }

            // params for generated typescript method:
            let methodParams: MethodParam[] = [];
            let queryParams: MethodParam[] = []; // subset of methodParams

            // -> required params:
            let hasQueryParams = false;
            // TODO if param name == dto Name - "dto" plus "Id": skip param and derive from dto
            if (operation.parameters) {
              // path params:
              for (let opParam of operation.parameters.filter(opp => opp.in === 'path')) {
                methodParams.push({
                  paramName: opParam.name,
                  paramType: sanitizeParamType(opParam.schema.type),
                  paramOpt: false,
                  paramNote: 'Path Variable'
                })
              }

              // query params - required:
              for (let opParam of operation.parameters.filter(opp => opp.in === 'query' && opp.required === true)) {
                hasQueryParams = true;
                let queryParam: MethodParam = {
                  paramName: opParam.name,
                  paramType: sanitizeParamType(opParam.schema.type),
                  paramOpt: false
                };
                methodParams.push(queryParam)
                queryParams.push(queryParam)
              }
            }

            // -> dto, if request body present
            if (requestBodyType != null) {
              methodParams.push({
                paramType: sanitizeParamType(requestBodyType),
                paramName: requestBodyVarName,
                paramOpt: false
              })
            }

            // -> optional params (must be lasted)
            if (operation.parameters) {
              // query params - optional:
              for (let opParam of operation.parameters.filter(opp => opp.in === 'query' && opp.required === false)) {
                hasQueryParams = true
                let queryParam: MethodParam = {
                  paramName: opParam.name,
                  paramType: sanitizeParamType(opParam.schema.type),
                  paramOpt: true
                };
                methodParams.push(queryParam)
                queryParams.push(queryParam)
              }
            }

            let isTextResponse: boolean = returnType === 'string'

            operationsByTag.get(tag).push({
              withAsync,
              path: path.replace(/{([^}]+)}/g, '${$1}'), // replace {foo} with ${foo}
              operationId: trimSuffix(operation.operationId),
              method,
              returnType,
              isReturnTypeArray,
              isTextResponse,
              methodParams,
              hasRequestBody,
              requestBodyType,
              requestBodyVarName,
              hasQueryParams,
              queryParams,
              hasOptionsObject: hasQueryParams || isTextResponse
            });
          });
        }
      });
    });
  }

  console.log(operationsByTag);

  return operationsByTag;
}

function sanitizeParamType(type: string): string {
  if (type === 'integer') type = 'number'

  return type
}

function convertToPascalCase(str) {
  return str
    .replace(/(^\w|-\w)/g, clearAndUpper)
    .replace(/-/g, '');

  function clearAndUpper(text) {
    return text.replace(/-/, '').toUpperCase();
  }
}

function isArray(value) {
  return Object.prototype.toString.call(value) === '[object Array]';
}

function cloneDeep(value) {
  if (value === null || typeof value !== 'object') {
    return value; // Return the value if it's not an object or array
  }

  if (Array.isArray(value)) {
    // If it's an array, recursively clone each element
    return value.map(item => cloneDeep(item));
  }

  // If it's an object, create a new object and recursively clone each property
  const result = {};
  for (const key in value) {
    if (value.hasOwnProperty(key)) {
      result[key] = cloneDeep(value[key]);
    }
  }

  return result;
}

/**
 * Trims the end of the string after an underscore if it ends with a pattern like _1, _2, _3, etc.
 * @param input - The input string to be processed.
 * @returns The processed string with the pattern removed, or the original string if no pattern is found.
 */
function trimSuffix(input: string): string {
  // Regular expression to match the pattern _1, _2, _3, etc., at the end of the string
  const pattern = /_(\d+)$/;

  // Check if the input string ends with the pattern and replace it
  return input.replace(pattern, '');
}

function deduplicate(arr: any[]) {
  return Array.from(new Set(arr))
}
