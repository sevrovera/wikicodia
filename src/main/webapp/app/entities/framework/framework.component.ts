import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFramework } from 'app/shared/model/framework.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FrameworkService } from './framework.service';
import { FrameworkDeleteDialogComponent } from './framework-delete-dialog.component';

@Component({
  selector: 'jhi-framework',
  templateUrl: './framework.component.html',
})
export class FrameworkComponent implements OnInit, OnDestroy {
  frameworks: IFramework[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected frameworkService: FrameworkService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.frameworks = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.frameworkService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IFramework[]>) => this.paginateFrameworks(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.frameworks = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFrameworks();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFramework): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFrameworks(): void {
    this.eventSubscriber = this.eventManager.subscribe('frameworkListModification', () => this.reset());
  }

  delete(framework: IFramework): void {
    const modalRef = this.modalService.open(FrameworkDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.framework = framework;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFrameworks(data: IFramework[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.frameworks.push(data[i]);
      }
    }
  }
}
