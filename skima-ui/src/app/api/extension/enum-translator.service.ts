import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { allActivityMetric } from './api-extension';
import { ActivityMetric } from '../model/api.types';

// TODO this whole thing should be generated too

export interface EnumOption<E extends string> {
  value: E;
  label: string;
}

export type EnumOptionMap<E extends string> = Map<E, string>;

export function toMap<E extends string>(ops: EnumOption<E>[]): EnumOptionMap<E> {
  return new Map(ops.map(op => [op.value, op.label]));
}


@Injectable({
  providedIn: 'root',
})
export class EnumTranslatorService {
  constructor(private translateService: TranslateService) {}

  /**
   * @param enumFullyQualifiedKey e.g. MyEnum.default
   * @param enumValues values of the enum that should be present in the map
   */
  translate<E extends string>(enumFullyQualifiedKey: string, enumValues: E[]): EnumOption<E>[] {
    return enumValues.map((val: E) => {
      // e.g.: ENUMS.default.MY_ENUM_VALUE
      let opt: EnumOption<E> = {
        value: val,
        label: this.translateService.instant(`ENUMS.${enumFullyQualifiedKey}.${val}`),
      };
      return opt;
    });
  }

  /**
   * @param enumKey e.g. "default" for ENUMS.ActivityMetric.default
   */
  activityMetric(enumKey: string = 'default'): EnumOption<ActivityMetric>[] {
    // TODO cache
    return this.translate('ActivityMetric.' + enumKey, allActivityMetric);
  }
}
