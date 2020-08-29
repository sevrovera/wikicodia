import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikicodiaTestModule } from '../../../test.module';
import { MyArticlesDetailComponent } from 'app/entities/my-articles/my-articles-detail.component';
import { MyArticles } from 'app/shared/model/my-articles.model';

describe('Component Tests', () => {
  describe('MyArticles Management Detail Component', () => {
    let comp: MyArticlesDetailComponent;
    let fixture: ComponentFixture<MyArticlesDetailComponent>;
    const route = ({ data: of({ myArticles: new MyArticles(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikicodiaTestModule],
        declarations: [MyArticlesDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(MyArticlesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MyArticlesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load myArticles on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.myArticles).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
