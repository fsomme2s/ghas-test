import { AbstractControl, FormArray, FormControl, FormGroup } from '@angular/forms';

export type ModelFormGroup<T> = FormGroup<FormModel<T>>;

export type FormModel<T> = {
  [K in keyof T]: T[K] extends any[]
    ? (  // note for arrays: we expect FormArray<ModelFormGroup<X>> - not ...<X[]>
      T[K] extends (string|number)[]
        ? FormArray<FormControl<UnwrapArray<T[K]>>> // primitive arrays - using FormControl
        : FormArray<ModelFormGroup<UnwrapArray<T[K]>>> // object arrays - using FormGroup
      )
    : AbstractControl<T[K]>; // normal fields
};

// Utility type to unwrap array types
export type UnwrapArray<T> = T extends (infer U)[] ? U : T;


export function createFormGroup<M>(fields: FormModel<M>): ModelFormGroup<M> {
  return new FormGroup<FormModel<M>>(fields);
}

export function createFormArray<M>(): FormArray<ModelFormGroup<M>> {
  return new FormArray<ModelFormGroup<M>>([]);
}

export function createFormArrayPrimitive<M extends string|number>(): FormArray<FormControl<M>> {
  return new FormArray<FormControl<M>>([]);
}
