import { Injectable, Injector } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AuthService } from '../auth/auth.service';
import { SkimaToastService } from './skima-toast.service';
import { UnexpectedErrorDto, UserErrorDto, ValidationErrorDto } from '../../api/model/api.types';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root',
})
export class HttpErrorInterceptor implements HttpInterceptor {
  constructor(
    //
    private toastService: SkimaToastService,
    private authService: AuthService,
    private injector: Injector,
  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      tap(
        () => {},
        (err: any) => {
          if (!window.navigator.onLine) {
            this.showNoConnection();
          } else {
            this.handleErrorStatus(request, err);
          }

          /*
           * For some reason, HttpErrorResponse.error does not automatically get parsed as json object,
           * although the content type header of the response is set to json.
           * To workaround this bug, we'll do it manually here. Since error property is readonly, we must
           * create a new HttpErrorResponse instance.
           */
          if (
            err instanceof HttpErrorResponse &&
            err.headers?.get('content-type')?.includes('application/json') &&
            typeof err.error === 'string'
          ) {
            throw new HttpErrorResponse({
              error: JSON.parse(err.error), // <-- this is our point of interest
              headers: err.headers,
              url: err.url || undefined,
              status: err.status,
              statusText: err.statusText,
            });
          }
        },
      ),
    );
  }

  private handleErrorStatus(request: HttpRequest<any>, httpErr: any) {
    const translate = this.injector.get(TranslateService);

    switch (httpErr.status) {
      case 400:
        console.log(httpErr.error);
        if (httpErr.error.type === 'USER') {
          const userErrorDto = httpErr.error as UserErrorDto;
          this.toastService.showError(translate.instant('ERROR.USER.' + userErrorDto.errorCode, userErrorDto.data));
        } else if (httpErr.error.type === 'VALIDATION') {
          const validationErrorDto = httpErr.error as ValidationErrorDto;

          const errorsString = validationErrorDto.validationErrors
            .map(valErr => `"${valErr.field}": "${valErr.message}"`)
            .join("; ");

          this.toastService.showError(translate.instant('ERROR.VALIDATION', { errors: errorsString }));
        }

        break;
      case 401:
        if (request.url !== '/api/login') {
          // On Login-API Requests, this error is not a session-timeout
          // (in this case an error message on Loginview will be displayed).
          this.toastService.showError('Session Timed Out, please login again!');
        }
        this.authService.logout();
        break;
      case 403:
        this.toastService.showError('Missing Permission!');
        break;
      case 404:
        // This case should be handled by the http caller
        // this.toastService.showError('Seite oder Objekt nicht gefunden.')
        break;
      case 504:
        this.showNoConnection();
        break;
      default:
        // console instead of Logger-Service, because that might send an error-logging back to the server, but
        // we are already reacting to a server error. In worst-case this might lead to infinite ui-backend-loop.
        console.log(httpErr);
        if (httpErr.error.type === 'UNEXPECTED') {
          const errorDto = httpErr.error as UnexpectedErrorDto;

           
          console.error('Server Error:', errorDto.errorLogId);

          // switch's default case also includes 500 internal error, which might contain an error-log-id:
          this.toastService.showError(
            // TODO
            `Errorlog-ID: ${errorDto.errorLogId}`,
            // `Entschuldigung, ein Fehler ist aufgetreten!
            //       Bitte notieren Sie sich ungespeicherte Eingaben und versuchen Sie es erneut.
            //       Tritt der Fehler öfter auf, melden Sie ihn einem Administrator.
            //       (Fehlertyp: ${err.status}; Errorlog-ID: ${errorLogId}`,
          );
        } else {
           
          console.error('Server Error in unknown format:', httpErr);
          this.toastService.showError('An unexpected error occured!');
        }
        break;
    }
  }

  private showNoConnection() {
    this.toastService.showError('Server nicht erreichbar. Bitte prüfen Sie ihre Internetverbindung.');
  }
}
