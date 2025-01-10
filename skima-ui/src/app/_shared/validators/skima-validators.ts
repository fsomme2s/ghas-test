import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export class SkimaValidators {
  /**
   * like Angular Validators "required" but it trims string values and also checks numbers are not zero.
   */
  static notEmpty(control: AbstractControl): ValidationErrors | null {
    let value = control.value;
    if (typeof value === 'string') {
      value = value.trim();
    }
    return !value || value.length === 0 ? { required: true } : null;
  }

  static email(control: AbstractControl): ValidationErrors | null {
    let value = control.value;
    if (typeof value === 'string') {
      value = value.trim().toLowerCase();
    }
    if (value == null || value === '') {
      return null; // empty = not invalid
    }

    // Very simple and thus permissive email regexp:
    const emailRegExp = /\S+@\S+\.\S+/;
    if (!emailRegExp.test(value)) {
      return { customError: 'Keine gültige E-Mail Adresse!' };
    }

    return null;
  }

  static min(min: number): ValidatorFn {
    return (control: AbstractControl) => {
      let value = control.value;
      if (typeof value === 'string') {
        value = parseInt(value.trim());
      }
      if (typeof value !== 'number' || isNaN(value)) {
        return { custom: 'Keine gültige Zahl!' };
      }

      if (value < min) {
        return { custom: `Wert muss mindestens ${min} sein!` };
      }

      return null;
    };
  }

  static match(controlName: string, checkControlName: string): ValidatorFn {
    return (controls: AbstractControl) => {
      const control = controls.get(controlName);
      const checkControl = controls.get(checkControlName);
      if (control == null || checkControl == null) {
        return null;
      }
      if (control.value !== checkControl.value) {
        controls.get(checkControlName)?.setErrors({ custom: 'Müssen gleich sein' });
        return { custom: true };
      } else {
        return null;
      }
    };
  }

  /**
   * fixed version of angular's pattern validator.
   */
  static pattern(regExp: RegExp, patternExample: string, allowEmpty = false): ValidatorFn {
    return (control: AbstractControl) => {
      // Empty Value...
      if (control.value == null || control.value.trim() === '') {
        // might be allowed or not
        return allowEmpty ? null : { required: true };
      }

      const matches = control.value.match(regExp);
      if (matches == null) {
        return { custom: 'Format: ' + patternExample };
      }

      // valid
      return null;
    };
  }
}
