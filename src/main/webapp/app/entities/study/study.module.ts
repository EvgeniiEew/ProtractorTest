import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StudyComponent } from './list/study.component';
import { StudyDetailComponent } from './detail/study-detail.component';
import { StudyUpdateComponent } from './update/study-update.component';
import { StudyDeleteDialogComponent } from './delete/study-delete-dialog.component';
import { StudyRoutingModule } from './route/study-routing.module';

import { MatInputModule } from '@angular/material/input';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSelectModule } from '@angular/material/select';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

@NgModule({
  imports: [MatInputModule, MatAutocompleteModule, MatSelectModule, MatTabsModule, SharedModule, StudyRoutingModule],
  declarations: [StudyComponent, StudyDetailComponent, StudyUpdateComponent, StudyDeleteDialogComponent],
  entryComponents: [StudyDeleteDialogComponent],
})
export class StudyModule {}
