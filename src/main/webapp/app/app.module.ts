import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { WikicodiaSharedModule } from './shared/shared.module';
import { WikicodiaCoreModule } from './core/core.module';
import { WikicodiaAppRoutingModule } from './app-routing.module';
import { WikicodiaHomeModule } from './home/home.module';
import { WikicodiaEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';
import { Global } from './global';

@NgModule({
  imports: [
    BrowserModule,
    WikicodiaSharedModule,
    WikicodiaCoreModule,
    WikicodiaHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    WikicodiaEntityModule,
    WikicodiaAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  providers: [Global],
  bootstrap: [MainComponent],
})
export class WikicodiaAppModule {}
