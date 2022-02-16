import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StudentComponent } from './list/student.component';
import { StudentDetailComponent } from './detail/student-detail.component';
import { StudentUpdateComponent } from './update/student-update.component';
import { StudentDeleteDialogComponent } from './delete/student-delete-dialog.component';
import { StudentRoutingModule } from './route/student-routing.module';

import { MatInputModule } from '@angular/material/input';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSelectModule } from '@angular/material/select';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

@NgModule({
  imports: [MatInputModule, MatAutocompleteModule, MatSelectModule, MatTabsModule, SharedModule, StudentRoutingModule],
  declarations: [StudentComponent, StudentDetailComponent, StudentUpdateComponent, StudentDeleteDialogComponent],
  entryComponents: [StudentDeleteDialogComponent],
})
export class StudentModule {}
