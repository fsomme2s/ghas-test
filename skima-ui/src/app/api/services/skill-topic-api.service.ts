import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SkillTopicDto } from '../model/api.types';



/**
 * API around {@link SkillTopic SkillTopic}
 */
@Injectable({
  providedIn: 'root',
})
export class SkillTopicApi {
  constructor(
    //
    private http: HttpClient,
  ) {}


  getSkillTopics(
  ): Observable<SkillTopicDto[]> {

    return this.http.get<SkillTopicDto[]>(
      `${environment.apiUrl}/api/skilltopics`,
      
    );
  }

    async getSkillTopics$(
    ): Promise<SkillTopicDto[]> {

      return lastValueFrom(this.http.get<SkillTopicDto[]>(
      `${environment.apiUrl}/api/skilltopics`,
      
      ));
      }




  createSkillTopic(
      skillTopicDto: SkillTopicDto,
  ): Observable<SkillTopicDto> {

    return this.http.post<SkillTopicDto>(
      `${environment.apiUrl}/api/skilltopics`,
      skillTopicDto,
    );
  }

    async createSkillTopic$(
      skillTopicDto: SkillTopicDto,
    ): Promise<SkillTopicDto> {

      return lastValueFrom(this.http.post<SkillTopicDto>(
      `${environment.apiUrl}/api/skilltopics`,
      skillTopicDto,
      ));
      }




  getSkillTopicDetails(
      skillTopicId: number,
  ): Observable<SkillTopicDto> {

    return this.http.get<SkillTopicDto>(
      `${environment.apiUrl}/api/skilltopics/${skillTopicId}`,
      
    );
  }

    async getSkillTopicDetails$(
      skillTopicId: number,
    ): Promise<SkillTopicDto> {

      return lastValueFrom(this.http.get<SkillTopicDto>(
      `${environment.apiUrl}/api/skilltopics/${skillTopicId}`,
      
      ));
      }



}
