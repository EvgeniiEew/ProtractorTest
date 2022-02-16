import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStudy, Study } from '../study.model';
import { StudyService } from '../service/study.service';

@Injectable({ providedIn: 'root' })
export class StudyRoutingResolveService implements Resolve<IStudy> {
  constructor(protected service: StudyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudy> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((study: HttpResponse<Study>) => {
          if (study.body) {
            return of(study.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Study());
  }
}
