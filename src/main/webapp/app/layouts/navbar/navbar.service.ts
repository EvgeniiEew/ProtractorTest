import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class NavbarService {
  public visiableMenu$ = new Subject<boolean>();

  public toggleHiddenMenu(toggle: boolean): void {
    this.visiableMenu$.next(toggle);
  }
}
