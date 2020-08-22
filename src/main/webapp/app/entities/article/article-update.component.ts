import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IArticle, Article } from 'app/shared/model/article.model';
import { ArticleService } from './article.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IVote } from 'app/shared/model/vote.model';
import { VoteService } from 'app/entities/vote/vote.service';
import { ILanguage } from 'app/shared/model/language.model';
import { LanguageService } from 'app/entities/language/language.service';
import { IFramework } from 'app/shared/model/framework.model';
import { FrameworkService } from 'app/entities/framework/framework.service';

type SelectableEntity = ICategory | IUser | IVote | ILanguage | IFramework;

type SelectableManyToManyEntity = IVote | ILanguage | IFramework;

@Component({
  selector: 'jhi-article-update',
  templateUrl: './article-update.component.html',
})
export class ArticleUpdateComponent implements OnInit {
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
    creationDate: [null, [Validators.required]],
    lastEditDate: [],
    description: [null, [Validators.required, Validators.minLength(20), Validators.maxLength(500)]],
    isPublished: [null, [Validators.required]],
    isValidated: [],
    isPromoted: [null, [Validators.required]],
    articleType: [null, [Validators.required]],
    category: [null, Validators.required],
    author: [null, Validators.required],
    ratings: [],
    languages: [null, Validators.required],
    frameworks: [],
  });

  constructor(
    protected articleService: ArticleService,
    protected categoryService: CategoryService,
    protected userService: UserService,
    protected voteService: VoteService,
    protected languageService: LanguageService,
    protected frameworkService: FrameworkService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ article }) => {
      this.updateForm(article);

      this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.voteService.query().subscribe((res: HttpResponse<IVote[]>) => (this.votes = res.body || []));

      this.languageService.query().subscribe((res: HttpResponse<ILanguage[]>) => (this.languages = res.body || []));

      this.frameworkService.query().subscribe((res: HttpResponse<IFramework[]>) => (this.frameworks = res.body || []));
    });
  }

  updateForm(article: IArticle): void {
    this.editForm.patchValue({
      id: article.id,
      title: article.title,
      creationDate: article.creationDate,
      lastEditDate: article.lastEditDate,
      description: article.description,
      isPublished: article.isPublished,
      isValidated: article.isValidated,
      isPromoted: article.isPromoted,
      articleType: article.articleType,
      category: article.category,
      author: article.author,
      ratings: article.ratings,
      languages: article.languages,
      frameworks: article.frameworks,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const article = this.createFromForm();
    if (article.id !== undefined) {
      this.subscribeToSaveResponse(this.articleService.update(article));
    } else {
      this.subscribeToSaveResponse(this.articleService.create(article));
    }
  }

  private createFromForm(): IArticle {
    return {
      ...new Article(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value,
      lastEditDate: this.editForm.get(['lastEditDate'])!.value,
      description: this.editForm.get(['description'])!.value,
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
