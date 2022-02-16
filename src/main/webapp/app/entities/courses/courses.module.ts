import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CoursesComponent } from './list/courses.component';
import { CoursesDetailComponent } from './detail/courses-detail.component';
import { CoursesUpdateComponent } from './update/courses-update.component';
import { CoursesDeleteDialogComponent } from './delete/courses-delete-dialog.component';
import { CoursesRoutingModule } from './route/courses-routing.module';

import { MatInputModule } from '@angular/material/input';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSelectModule } from '@angular/material/select';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

@NgModule({
  imports: [MatInputModule, MatAutocompleteModule, MatSelectModule, MatTabsModule, SharedModule, CoursesRoutingModule],
  declarations: [CoursesComponent, CoursesDetailComponent, CoursesUpdateComponent, CoursesDeleteDialogComponent],
  entryComponents: [CoursesDeleteDialogComponent],
})
export class CoursesModule {}
