import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikicodiaTestModule } from '../../../test.module';
import { ArticleDetailComponent } from 'app/entities/article/article-detail.component';
import { Article } from 'app/shared/model/article.model';

describe('Component Tests', () => {
  describe('Article Management Detail Component', () => {
    let comp: ArticleDetailComponent;
    let fixture: ComponentFixture<ArticleDetailComponent>;
    const route = ({ data: of({ article: new Article(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikicodiaTestModule],
        declarations: [ArticleDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ArticleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ArticleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load article on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.article).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
