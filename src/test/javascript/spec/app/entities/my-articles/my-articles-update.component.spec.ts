import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikicodiaTestModule } from '../../../test.module';
import { MyArticlesUpdateComponent } from 'app/entities/my-articles/my-articles-update.component';
import { MyArticlesService } from 'app/entities/my-articles/my-articles.service';
import { MyArticles } from 'app/shared/model/my-articles.model';

describe('Component Tests', () => {
  describe('MyArticles Management Update Component', () => {
    let comp: MyArticlesUpdateComponent;
    let fixture: ComponentFixture<MyArticlesUpdateComponent>;
    let service: MyArticlesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikicodiaTestModule],
        declarations: [MyArticlesUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(MyArticlesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MyArticlesUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MyArticlesService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MyArticles(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new MyArticles();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
