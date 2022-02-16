import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudyComponent } from '../list/study.component';
import { StudyDetailComponent } from '../detail/study-detail.component';
import { StudyUpdateComponent } from '../update/study-update.component';
import { StudyRoutingResolveService } from './study-routing-resolve.service';

const studyRoute: Routes = [
  {
    path: '',
    component: StudyComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StudyDetailComponent,
    resolve: {
      study: StudyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StudyUpdateComponent,
    resolve: {
      study: StudyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StudyUpdateComponent,
    resolve: {
      study: StudyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(studyRoute)],
  exports: [RouterModule],
})
export class StudyRoutingModule {}
