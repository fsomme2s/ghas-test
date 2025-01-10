import { UserErrorDto } from '../../api/model/api.types';
import { HttpErrorResponse } from '@angular/common/http';

export class ErrorUtil {
  static extractUserError(err: any): UserErrorDto | null {
    if (err instanceof HttpErrorResponse && err.error.type === 'USER')
      return err.error as UserErrorDto;
    if (err.type === 'USER' && err.errorCode != null) return err as UserErrorDto;
    return null;
  }
}
