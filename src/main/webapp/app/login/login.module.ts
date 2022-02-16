import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';

import { SharedModule } from 'app/shared/shared.module';
import { LOGIN_ROUTE } from './login.route';

import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  imports: [ReactiveFormsModule, MatButtonModule, MatInputModule, SharedModule, RouterModule.forChild([LOGIN_ROUTE])],
  declarations: [],
})
export class LoginModule {}
