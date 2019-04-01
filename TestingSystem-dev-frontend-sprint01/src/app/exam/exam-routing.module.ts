import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ListExamComponent } from './list-exam/list-exam.component';
import { CreateExamComponent } from './create-exam/create-exam.component';
import { DetailExamComponent } from './detail-exam/detail-exam.component';
import { UpdateExamComponent } from './update-exam/update-exam.component';
import { UpdateCommonComponent } from './update-common/update-common.component';
import { UpdateContentComponent } from './update-content/update-content.component';

const routes: Routes = [
  // {
  //   path: 'exam',
  //   children: [
  //     {
  //       path: '',
  //       component: ListExamComponent,
  //       pathMatch: 'full'
  //     },
  //     {
  //       path: 'create',
  //       component: CreateExamComponent
  //     },
  //     {
  //       path: ':id',
  //       component: DetailExamComponent
  //     },
  //     {
  //       path: ':id/update',
  //       component: UpdateExamComponent
  //     },
  //     {
  //       path: ':id/update/update-common',
  //       component: UpdateCommonComponent
  //     },
  //     {
  //       path: ':id/update/update-content',
  //       component: UpdateContentComponent
  //     }
  //   ]
  // }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ExamRoutingModule {}
