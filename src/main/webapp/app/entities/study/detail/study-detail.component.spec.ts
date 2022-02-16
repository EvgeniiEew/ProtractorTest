import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StudyDetailComponent } from './study-detail.component';

describe('Component Tests', () => {
  describe('Study Management Detail Component', () => {
    let comp: StudyDetailComponent;
    let fixture: ComponentFixture<StudyDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [StudyDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ study: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(StudyDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudyDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load study on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.study).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
