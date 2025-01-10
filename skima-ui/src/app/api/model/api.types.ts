/* eslint-disable */
/* tslint:disable */
/*
 * ---------------------------------------------------------------
 * ## THIS FILE WAS GENERATED VIA SWAGGER-TYPESCRIPT-API        ##
 * ##                                                           ##
 * ## AUTHOR: acacode                                           ##
 * ## SOURCE: https://github.com/acacode/swagger-typescript-api ##
 * ---------------------------------------------------------------
 */

/** Dto for {@link SkillTopic SkillTopic} */
export interface SkillTopicDto {
  /** @format int64 */
  id?: number | null;
  title: string;
  certifications: string[];
}

export type ActivityMetric = 'CHAPTERS' | 'WEB_PAGES' | 'TODOS' | 'CUSTOM';

/** Dto for {@link TrainingActivity TrainingActivity} */
export interface TrainingActivityDto {
  /** @format int64 */
  id?: number | null;
  title: string;
  metric: ActivityMetric;
  /**
   * Only used if this.{@link de.conet.isd.skima.skimabackend.api.ui.skilltopics.dto.TrainingActivityDto#metric #metric} does not {@link ActivityMetric#isRequiresTaskLists require a task lists};
   *  otherwise {@link de.conet.isd.skima.skimabackend.api.ui.skilltopics.dto.TrainingActivityDto#subTasks #subTasks} is used.
   * @format double
   */
  targetAmount: number;
  /**
   * Only used if this.{@link de.conet.isd.skima.skimabackend.api.ui.skilltopics.dto.TrainingActivityDto#metric #metric} does {@link ActivityMetric#isRequiresTaskLists require a task lists};
   *  otherwise {@link de.conet.isd.skima.skimabackend.api.ui.skilltopics.dto.TrainingActivityDto#targetAmount #targetAmount} is used.
   */
  subTasks: string[];
}

/** Send to backend for login */
export interface LoginDto {
  username: string;
  secret: string;
}

/** The operating environment that this instance is running on */
export type SkimaEnvironment =
  | 'LOCAL'
  | 'QA_CLOUD'
  | 'QA_ENTERPRISE'
  | 'PROD_CLOUD'
  | 'PROD_ENTERPRISE';

/** Contains Details about System Status */
export interface StatusDto {
  /** The operating environment that this instance is running on */
  env: SkimaEnvironment;
  /** {@see AppConfig#version()} */
  version: string;
  /**
   * {@see AppConfig#versionTimestamp()}
   * @format date-time
   */
  versionTimestamp: string;
}

/** Base for Error Dtos, meant to be processed by our UI. Field "type" shows which specific error type this is. */
export type ErrorDto = BaseErrorDto &
  (
    | BaseErrorDtoTypeMapping<'UNEXPECTED', UnexpectedErrorDto>
    | BaseErrorDtoTypeMapping<'USER', UserErrorDto>
    | BaseErrorDtoTypeMapping<'VALIDATION', ValidationErrorDto>
  );

/**
 * Response for Unexpected Errors on Server Side. Those usually require manual investigation and fixes
 *  which is part of system maintenance. To support this task, they contain a random Id, which is logged alongside
 *  the exception, which makes it easier to find the stacktrace after a user reported the error.
 */
export interface UnexpectedErrorDto {
  errorLogId: string;
}

export interface ValidationErrorDto {
  validationErrors: FieldValidationErrorDto[];
}

export interface FieldValidationErrorDto {
  field: string;
  message: string;
}

/**
 * Error that is meant to be displayed to the user; contains an error code . The Client does not have to
 *  parse and react to the message (although it could do so if required, of course), but most of the times you
 *  just translate the ERROR_CODE into a Text to display.
 */
export interface UserErrorDto {
  errorCode: any;
  data: Record<string, object>;
}

/** Base for Error Dtos, meant to be processed by our UI. Field "type" shows which specific error type this is. */
interface BaseErrorDto {
  type: string;
}

type BaseErrorDtoTypeMapping<Key, Type> = {
  type: Key;
} & Type;

export interface ProvokeErrorParams {
  /**
   * Which Error type to provoke; {@link ErrorDto ErrorDto}.type values can be used.
   *              If the type is unkonwn, a Bad Request with technical error description will be thrown.
   * @default "UNEXPECTED"
   */
  type: string;
}
