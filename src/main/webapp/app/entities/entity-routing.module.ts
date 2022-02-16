import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'courses',
        data: { pageTitle: 'coursesApp.courses.home.title' },
        loadChildren: () => import('./courses/courses.module').then(m => m.CoursesModule),
      },
      {
        path: 'provider',
        data: { pageTitle: 'coursesApp.provider.home.title' },
        loadChildren: () => import('./provider/provider.module').then(m => m.ProviderModule),
      },
      {
        path: 'student',
        data: { pageTitle: 'coursesApp.student.home.title' },
        loadChildren: () => import('./student/student.module').then(m => m.StudentModule),
      },
      {
        path: 'study',
        data: { pageTitle: 'coursesApp.study.home.title' },
        loadChildren: () => import('./study/study.module').then(m => m.StudyModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
