import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';
import { StatusDto } from '../model/api.types';



/**
 * Api for System Status &amp; technical tests / pocs
 */
@Injectable({
  providedIn: 'root',
})
export class StatusApi {
  constructor(
    //
    private http: HttpClient,
  ) {}


  getStatus(
  ): Observable<StatusDto> {

    return this.http.get<StatusDto>(
      `${environment.apiUrl}/api/status`,
      
    );
  }

    async getStatus$(
    ): Promise<StatusDto> {

      return lastValueFrom(this.http.get<StatusDto>(
      `${environment.apiUrl}/api/status`,
      
      ));
      }




  provokeError(
      type?: string,
  ): Observable<void> {
      let params = {};
        if (type != null)  params = { ...params, type }

    return this.http.get<void>(
      `${environment.apiUrl}/api/status/provoke-error`,
      
        {
          params,
          
        }
    );
  }

    async provokeError$(
      type?: string,
    ): Promise<void> {
      let params = {};
          if (type != null)  params = { ...params, type }

      return lastValueFrom(this.http.get<void>(
      `${environment.apiUrl}/api/status/provoke-error`,
      
      {
          params,
        
        }
      ));
      }



}
