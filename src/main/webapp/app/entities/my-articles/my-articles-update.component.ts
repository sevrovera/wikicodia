import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMyArticles, MyArticles } from '../../shared/model/my-articles.model';
import { IArticle, Article } from '../../shared/model/article.model';
import { MyArticlesService } from './my-articles.service';
import { ICategory } from '../../shared/model/category.model';
import { CategoryService } from '../../entities/category/category.service';
import { IUser } from '../../core/user/user.model';
import { UserService } from '../../core/user/user.service';
import { IVote } from '../../shared/model/vote.model';
import { VoteService } from '../../entities/vote/vote.service';
import { ILanguage } from '../../shared/model/language.model';
import { LanguageService } from '../../entities/language/language.service';
import { IFramework } from '../../shared/model/framework.model';
import { FrameworkService } from '../../entities/framework/framework.service';

type SelectableEntity = ICategory | IUser | IVote | ILanguage | IFramework;

type SelectableManyToManyEntity = IVote | ILanguage | IFramework;

@Component({
  selector: 'jhi-my-articles-update',
  templateUrl: './my-articles-update.component.html',
})
export class MyArticlesUpdateComponent implements OnInit {
  isSaving = false;
  categories: ICategory[] = [];
  users: IUser[] = [];
  votes: IVote[] = [];
  languages: ILanguage[] = [];
  frameworks: IFramework[] = [];
  creationDateDp: any;
  lastEditDateDp: any;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.minLength(5)]],
    creationDate: [],
    lastEditDate: [],
    description: [null, [Validators.required, Validators.minLength(20), Validators.maxLength(500)]],
    content: [null, [Validators.required, Validators.minLength(200), Validators.maxLength(65535)]],
    isPublished: [],
    isValidated: [],
    isPromoted: [],
    articleType: [null, [Validators.required]],
    category: [null, Validators.required],
    author: [null, Validators.required],
    ratings: [],
    languages: [null, Validators.required],
    frameworks: [],
  });

  constructor(
    protected myArticlesService: MyArticlesService,
    protected categoryService: CategoryService,
    protected userService: UserService,
    protected voteService: VoteService,
    protected languageService: LanguageService,
    protected frameworkService: FrameworkService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ myArticles }) => {
      this.updateForm(myArticles);

      this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.voteService.query().subscribe((res: HttpResponse<IVote[]>) => (this.votes = res.body || []));

      this.languageService.query().subscribe((res: HttpResponse<ILanguage[]>) => (this.languages = res.body || []));

      this.frameworkService.query().subscribe((res: HttpResponse<IFramework[]>) => (this.frameworks = res.body || []));
    });
  }

  //updateForm(myArticles: IMyArticles): void {
  updateForm(myArticles: IArticle): void {
    this.editForm.patchValue({
      id: myArticles.id,
      title: myArticles.title,
      creationDate: myArticles.creationDate,
      lastEditDate: myArticles.lastEditDate,
      description: myArticles.description,
      content: myArticles.content,
      isPublished: myArticles.isPublished,
      isValidated: myArticles.isValidated,
      isPromoted: myArticles.isPromoted,
      articleType: myArticles.articleType,
      category: myArticles.category,
      author: myArticles.author,
      ratings: myArticles.ratings,
      languages: myArticles.languages,
      frameworks: myArticles.frameworks,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const myArticles = this.createFromForm();
    if (myArticles.id !== undefined) {
      this.subscribeToSaveResponse(this.myArticlesService.update(myArticles));
    } else {
      this.subscribeToSaveResponse(this.myArticlesService.create(myArticles));
    }
  }

  //private createFromForm(): IMyArticles {
  private createFromForm(): IArticle {
    return {
      //...new MyArticles(),
      ...new Article(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value,
      lastEditDate: this.editForm.get(['lastEditDate'])!.value,
      description: this.editForm.get(['description'])!.value,
      content: this.editForm.get(['content'])!.value,
      isPublished: this.editForm.get(['isPublished'])!.value,
      isValidated: this.editForm.get(['isValidated'])!.value,
      isPromoted: this.editForm.get(['isPromoted'])!.value,
      articleType: this.editForm.get(['articleType'])!.value,
      category: this.editForm.get(['category'])!.value,
      author: this.editForm.get(['author'])!.value,
      ratings: this.editForm.get(['ratings'])!.value,
      languages: this.editForm.get(['languages'])!.value,
      frameworks: this.editForm.get(['frameworks'])!.value,
    };
  }

  //protected subscribeToSaveResponse(result: Observable<HttpResponse<IMyArticles>>): void {
  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArticle>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: SelectableManyToManyEntity[], option: SelectableManyToManyEntity): SelectableManyToManyEntity {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
