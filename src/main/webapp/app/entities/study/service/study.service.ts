import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStudy, getStudyIdentifier } from '../study.model';

export type EntityResponseType = HttpResponse<IStudy>;
export type EntityArrayResponseType = HttpResponse<IStudy[]>;

@Injectable({ providedIn: 'root' })
export class StudyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/studies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(study: IStudy): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(study);
    return this.http
      .post<IStudy>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(study: IStudy): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(study);
    return this.http
      .put<IStudy>(`${this.resourceUrl}/${getStudyIdentifier(study) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(study: IStudy): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(study);
    return this.http
      .patch<IStudy>(`${this.resourceUrl}/${getStudyIdentifier(study) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStudy>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStudy[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStudyToCollectionIfMissing(studyCollection: IStudy[], ...studiesToCheck: (IStudy | null | undefined)[]): IStudy[] {
    const studies: IStudy[] = studiesToCheck.filter(isPresent);
    if (studies.length > 0) {
      const studyCollectionIdentifiers = studyCollection.map(studyItem => getStudyIdentifier(studyItem)!);
      const studiesToAdd = studies.filter(studyItem => {
        const studyIdentifier = getStudyIdentifier(studyItem);
        if (studyIdentifier == null || studyCollectionIdentifiers.includes(studyIdentifier)) {
          return false;
        }
        studyCollectionIdentifiers.push(studyIdentifier);
        return true;
      });
      return [...studiesToAdd, ...studyCollection];
    }
    return studyCollection;
  }

  protected convertDateFromClient(study: IStudy): IStudy {
    return Object.assign({}, study, {
      dateOfStart: study.dateOfStart?.isValid() ? study.dateOfStart.format(DATE_FORMAT) : undefined,
      dateOfExam: study.dateOfExam?.isValid() ? study.dateOfExam.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfStart = res.body.dateOfStart ? dayjs(res.body.dateOfStart) : undefined;
      res.body.dateOfExam = res.body.dateOfExam ? dayjs(res.body.dateOfExam) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((study: IStudy) => {
        study.dateOfStart = study.dateOfStart ? dayjs(study.dateOfStart) : undefined;
        study.dateOfExam = study.dateOfExam ? dayjs(study.dateOfExam) : undefined;
      });
    }
    return res;
  }
}
