import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginDto } from '../model/api.types';



/**
 * 
 */
@Injectable({
  providedIn: 'root',
})
export class LoginApi {
  constructor(
    //
    private http: HttpClient,
  ) {}


  refreshToken(
  ): Observable<string> {

    return this.http.post(
      `${environment.apiUrl}/api/refresh-token`,
      null,
        {
          
          responseType: 'text',
        }
    );
  }

    async refreshToken$(
    ): Promise<string> {

      return lastValueFrom(this.http.post(
      `${environment.apiUrl}/api/refresh-token`,
      null,
      {
          
        responseType: 'text',
        }
      ));
      }




  login(
      loginDto: LoginDto,
  ): Observable<string> {

    return this.http.post(
      `${environment.apiUrl}/api/login`,
      loginDto,
        {
          
          responseType: 'text',
        }
    );
  }

    async login$(
      loginDto: LoginDto,
    ): Promise<string> {

      return lastValueFrom(this.http.post(
      `${environment.apiUrl}/api/login`,
      loginDto,
      {
          
        responseType: 'text',
        }
      ));
      }



}
