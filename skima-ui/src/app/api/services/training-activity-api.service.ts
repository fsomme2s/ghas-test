import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';
import { TrainingActivityDto } from '../model/api.types';



/**
 * Api around {@link TrainingActivity TrainingActivity}
 */
@Injectable({
  providedIn: 'root',
})
export class TrainingActivityApi {
  constructor(
    //
    private http: HttpClient,
  ) {}


  getActiviesOfSkillTopic(
      skillTopicId: number,
  ): Observable<TrainingActivityDto[]> {

    return this.http.get<TrainingActivityDto[]>(
      `${environment.apiUrl}/api/skilltopics/${skillTopicId}/activities`,
      
    );
  }

    async getActiviesOfSkillTopic$(
      skillTopicId: number,
    ): Promise<TrainingActivityDto[]> {

      return lastValueFrom(this.http.get<TrainingActivityDto[]>(
      `${environment.apiUrl}/api/skilltopics/${skillTopicId}/activities`,
      
      ));
      }




  createActivity(
      skillTopicId: number,
      trainingActivityDto: TrainingActivityDto,
  ): Observable<TrainingActivityDto> {

    return this.http.post<TrainingActivityDto>(
      `${environment.apiUrl}/api/skilltopics/${skillTopicId}/activities`,
      trainingActivityDto,
    );
  }

    async createActivity$(
      skillTopicId: number,
      trainingActivityDto: TrainingActivityDto,
    ): Promise<TrainingActivityDto> {

      return lastValueFrom(this.http.post<TrainingActivityDto>(
      `${environment.apiUrl}/api/skilltopics/${skillTopicId}/activities`,
      trainingActivityDto,
      ));
      }




  deleteActivy(
      skillTopicId: number,
      activityId: number,
  ): Observable<void> {

    return this.http.delete<void>(
      `${environment.apiUrl}/api/skilltopics/${skillTopicId}/activities/${activityId}`,
      
    );
  }

    async deleteActivy$(
      skillTopicId: number,
      activityId: number,
    ): Promise<void> {

      return lastValueFrom(this.http.delete<void>(
      `${environment.apiUrl}/api/skilltopics/${skillTopicId}/activities/${activityId}`,
      
      ));
      }



}
