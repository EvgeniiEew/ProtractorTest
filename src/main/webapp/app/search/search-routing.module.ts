import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchComponent } from './search.component';

export const searchRoutes: Routes = [
  {
    path: '',
    component: SearchComponent,
  },
];

@NgModule({
  imports: [],
  exports: [RouterModule],
})
export class SearchRoutingModule {}
