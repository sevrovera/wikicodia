import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IVote, Vote } from 'app/shared/model/vote.model';
import { VoteService } from './vote.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-vote-update',
  templateUrl: './vote-update.component.html',
})
export class VoteUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    isLiked: [null, [Validators.required]],
    comment: [],
    author: [null, Validators.required],
  });

  constructor(
    protected voteService: VoteService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vote }) => {
      this.updateForm(vote);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(vote: IVote): void {
    this.editForm.patchValue({
      id: vote.id,
      isLiked: vote.isLiked,
      comment: vote.comment,
      author: vote.author,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vote = this.createFromForm();
    if (vote.id !== undefined) {
      this.subscribeToSaveResponse(this.voteService.update(vote));
    } else {
      this.subscribeToSaveResponse(this.voteService.create(vote));
    }
  }

  private createFromForm(): IVote {
    return {
      ...new Vote(),
      id: this.editForm.get(['id'])!.value,
      isLiked: this.editForm.get(['isLiked'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      author: this.editForm.get(['author'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVote>>): void {
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

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
