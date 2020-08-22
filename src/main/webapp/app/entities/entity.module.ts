import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'language',
        loadChildren: () => import('./language/language.module').then(m => m.WikicodiaLanguageModule),
      },
      {
        path: 'framework',
        loadChildren: () => import('./framework/framework.module').then(m => m.WikicodiaFrameworkModule),
      },
      {
        path: 'vote',
        loadChildren: () => import('./vote/vote.module').then(m => m.WikicodiaVoteModule),
      },
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.WikicodiaCategoryModule),
      },
      {
        path: 'article',
        loadChildren: () => import('./article/article.module').then(m => m.WikicodiaArticleModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class WikicodiaEntityModule {}
