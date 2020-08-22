import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFramework, Framework } from 'app/shared/model/framework.model';
import { FrameworkService } from './framework.service';

@Component({
  selector: 'jhi-framework-update',
  templateUrl: './framework-update.component.html',
})
export class FrameworkUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nameFramework: [null, [Validators.required]],
    version: [],
  });

  constructor(protected frameworkService: FrameworkService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ framework }) => {
      this.updateForm(framework);
    });
  }

  updateForm(framework: IFramework): void {
    this.editForm.patchValue({
      id: framework.id,
      nameFramework: framework.nameFramework,
      version: framework.version,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const framework = this.createFromForm();
    if (framework.id !== undefined) {
      this.subscribeToSaveResponse(this.frameworkService.update(framework));
    } else {
      this.subscribeToSaveResponse(this.frameworkService.create(framework));
    }
  }

  private createFromForm(): IFramework {
    return {
      ...new Framework(),
      id: this.editForm.get(['id'])!.value,
      nameFramework: this.editForm.get(['nameFramework'])!.value,
      version: this.editForm.get(['version'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFramework>>): void {
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
}
