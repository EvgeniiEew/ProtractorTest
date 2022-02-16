import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, CommonModule],
  exports: [],
})
export class MenuModule {}
