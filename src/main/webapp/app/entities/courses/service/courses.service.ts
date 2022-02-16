import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICourses, getCoursesIdentifier } from '../courses.model';

export type EntityResponseType = HttpResponse<ICourses>;
export type EntityArrayResponseType = HttpResponse<ICourses[]>;

@Injectable({ providedIn: 'root' })
export class CoursesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/courses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(courses: ICourses): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courses);
    return this.http
      .post<ICourses>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(courses: ICourses): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courses);
    return this.http
      .put<ICourses>(`${this.resourceUrl}/${getCoursesIdentifier(courses) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(courses: ICourses): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courses);
    return this.http
      .patch<ICourses>(`${this.resourceUrl}/${getCoursesIdentifier(courses) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICourses>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICourses[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCoursesToCollectionIfMissing(coursesCollection: ICourses[], ...coursesToCheck: (ICourses | null | undefined)[]): ICourses[] {
    const courses: ICourses[] = coursesToCheck.filter(isPresent);
    if (courses.length > 0) {
      const coursesCollectionIdentifiers = coursesCollection.map(coursesItem => getCoursesIdentifier(coursesItem)!);
      const coursesToAdd = courses.filter(coursesItem => {
        const coursesIdentifier = getCoursesIdentifier(coursesItem);
        if (coursesIdentifier == null || coursesCollectionIdentifiers.includes(coursesIdentifier)) {
          return false;
        }
        coursesCollectionIdentifiers.push(coursesIdentifier);
        return true;
      });
      return [...coursesToAdd, ...coursesCollection];
    }
    return coursesCollection;
  }

  protected convertDateFromClient(courses: ICourses): ICourses {
    return Object.assign({}, courses, {
      dateOfStart: courses.dateOfStart?.isValid() ? courses.dateOfStart.format(DATE_FORMAT) : undefined,
      dateOfEnd: courses.dateOfEnd?.isValid() ? courses.dateOfEnd.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfStart = res.body.dateOfStart ? dayjs(res.body.dateOfStart) : undefined;
      res.body.dateOfEnd = res.body.dateOfEnd ? dayjs(res.body.dateOfEnd) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((courses: ICourses) => {
        courses.dateOfStart = courses.dateOfStart ? dayjs(courses.dateOfStart) : undefined;
        courses.dateOfEnd = courses.dateOfEnd ? dayjs(courses.dateOfEnd) : undefined;
      });
    }
    return res;
  }
}
