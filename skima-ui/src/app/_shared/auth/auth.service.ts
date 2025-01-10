import { Injectable } from '@angular/core';
import { Observable, Subject, take } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { LoginApi } from '../../api/services/login-api.service';
import { JwtModel } from './auth.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  /**
   * The Period in which we check if our token is expiring soon. Should be smaller than the token lifetime.
   */
  static readonly CHECK_TOKEN_PERIOD_MINUTES = 2;

  /**
   * Tokens that expire in less than this time will be renewed. Should be greater than CHECK_TOKEN_PERIOD_MINUTES,
   * because otherwise the token might expire within two runs of the Check.
   */
  static readonly TOKEN_RENEWAL_ON_EXP_MINUTES = 5;

  /**
   * triggered, when the login state changes.
   * emits: true = user logged in, false = user not logged in
   */
  onLogin = new Subject<boolean>();

  constructor(
    //
    private loginApi: LoginApi,
    private router: Router,
  ) {
    this.checkTokenRenewal();
    this.startPeriodicTokenCheck();
  }

  protected startPeriodicTokenCheck() {
    setInterval(
      () => {
        this.checkTokenRenewal();
      },
      1000 * 60 * AuthService.CHECK_TOKEN_PERIOD_MINUTES,
    );
  }

  protected checkTokenRenewal() {
    if (!this.token) return;
    const parsed = this.parsedToken;
    if (parsed == null || parsed.exp == null) return;

    const expiresInSeconds = parsed.exp - Date.now() / 1000;
    // this.logger.debug('Token expires in Seconds: ' + expiresInSeconds)

    // if the token expires within the next 3 secs,
    // we won't try to renew it, because a slow renewal request might take longer leading to weird behaviour
    const deltaSeconds = 3;

    if (expiresInSeconds <= deltaSeconds) {
      // this.logger.debug('Cached Token is expired. User will be logged out with next request.')
      return;
    }
    if (0 < expiresInSeconds && expiresInSeconds < AuthService.TOKEN_RENEWAL_ON_EXP_MINUTES * 60) {
      this.refreshToken().pipe(take(1)).subscribe();
    }
  }

  get token(): string | null {
    return localStorage.getItem('token');
  }

  /**
   * @returns the parsed JWT or null if no token is there / there were problems parsing the token.
   */
  get parsedToken(): JwtModel | null {
    return this.parseJwt(this.token);
  }

  private parseJwt(token: string | null): JwtModel | null {
    if (token == null) {
      return null;
    }

    const base64Payload = token.split('.')[1];
    if (base64Payload == null) {
      return null;
    }

    return JSON.parse(atob(base64Payload));
  }

  set token(token: string) {
    localStorage.setItem('token', token);
  }

  clearToken() {
    localStorage.removeItem('token');

    this.onLogin.next(false);
  }

  isLoggedIn() {
    return this.token != null;
  }

  login(username: string, password: string): Observable<void> {
    return this.loginApi
      .login({ username, secret: password })
      .pipe(
        tap((token) => {
          console.log('logged in, token:', token);
          this.token = token as string;
          this.onLogin.next(this.token != '');
        }),
      )
      .pipe(map(() => {})); // don't leak token.
  }

  logout() {
    this.clearToken();
    this.router.navigateByUrl('/login');
  }

  refreshToken(): Observable<void> {
    return this.loginApi
      .refreshToken()
      .pipe(
        tap((token) => {
          this.token = token as string;
          this.onLogin.next(this.token != '');
        }),
      )
      .pipe(map(() => {})); // don't leak token.
  }
}
