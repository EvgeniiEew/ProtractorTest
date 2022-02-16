import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProviderComponent } from './list/provider.component';
import { ProviderDetailComponent } from './detail/provider-detail.component';
import { ProviderUpdateComponent } from './update/provider-update.component';
import { ProviderDeleteDialogComponent } from './delete/provider-delete-dialog.component';
import { ProviderRoutingModule } from './route/provider-routing.module';

import { MatInputModule } from '@angular/material/input';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSelectModule } from '@angular/material/select';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

@NgModule({
  imports: [MatInputModule, MatAutocompleteModule, MatSelectModule, MatTabsModule, SharedModule, ProviderRoutingModule],
  declarations: [ProviderComponent, ProviderDetailComponent, ProviderUpdateComponent, ProviderDeleteDialogComponent],
  entryComponents: [ProviderDeleteDialogComponent],
})
export class ProviderModule {}
