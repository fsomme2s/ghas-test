import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    //
    private authService: AuthService,
  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Do not intercept calls to Login URIs:
    if (request.url.includes('login')) {
      return next.handle(request);
    }

    // Add auth header with jwt if user is logged in and request is to api url
    const token = this.authService.token;
    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }

    return next.handle(request).pipe(
      tap({
        error: (error: any) => this.onHttpError(error),
      }),
    );
  }

  private onHttpError(error: any) {
    if (error instanceof HttpErrorResponse && error.status === 401) {
      this.authService.clearToken();
    }
  }
}
