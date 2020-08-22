import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WikicodiaSharedModule } from 'app/shared/shared.module';
import { FrameworkComponent } from './framework.component';
import { FrameworkDetailComponent } from './framework-detail.component';
import { FrameworkUpdateComponent } from './framework-update.component';
import { FrameworkDeleteDialogComponent } from './framework-delete-dialog.component';
import { frameworkRoute } from './framework.route';

@NgModule({
  imports: [WikicodiaSharedModule, RouterModule.forChild(frameworkRoute)],
  declarations: [FrameworkComponent, FrameworkDetailComponent, FrameworkUpdateComponent, FrameworkDeleteDialogComponent],
  entryComponents: [FrameworkDeleteDialogComponent],
})
export class WikicodiaFrameworkModule {}
