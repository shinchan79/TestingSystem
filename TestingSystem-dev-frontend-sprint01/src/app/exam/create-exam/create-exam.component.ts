import { Component, OnInit } from '@angular/core';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { FormBuilder, FormGroup, Validators, FormControl, FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Exam } from 'src/app/entity/Exam.interface';
import { v4 as uuid } from 'uuid';
import { Category } from '../../entity/Category.interface';
import { User } from 'src/app/entity/User.interface';
import { ExamQuestion } from 'src/app/entity/ExamQuestion.interface';
import { NotifierService } from 'angular-notifier';
import { TokenStorageService } from 'src/app/auth/token-storage.service';

@Component({
  selector: 'app-create-exam',
  templateUrl: './create-exam.component.html',
  styleUrls: ['./create-exam.component.css']
})

export class CreateExamComponent implements OnInit {
  user = {
    userId: this.token.getUserId(),
    fullName: '',
    email: '',
    mobile: '',
    password: '',
    status: 0
  };

  public Editor = ClassicEditor;
  examFrm: FormGroup;
  categories: Category[] = [];
  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private notifierService: NotifierService,
    private token: TokenStorageService
  ) {}
    ngOnInit() {
      this.http.get<Category[]>('http://localhost:8080/category/listCategories').subscribe(category => {
        this.categories = category;
      });
      this.examFrm = this.fb.group({
      title: ['', Validators.required],
      category: ['', Validators.required],
      numberquestion: ['', Validators.required],
      duration: ['', Validators.required],
      note: ['', Validators.required],
    });
  }
  onCreate() {
    // const user: User = null;
    const examQuestions: ExamQuestion[] = [];
    const date: Date = null ;
    const cate: Category = this.examFrm.get('category').value;
      const exam: Exam = {
        examId: uuid(),
        title: this.examFrm.get('title').value,
        duration: this.examFrm.get('duration').value,
        note: this.examFrm.get('note').value,
        status: 'Draft',
        categoryName: cate.categoryName,
        createAt: date,
        modifiedAt: date,
        numberOfQuestion: this.examFrm.get('numberquestion').value,
        category: cate,
         userCreated: this.user,
        modifiedBy:   this.user,
        examQuestions: examQuestions,
        enable: false,
      };
      console.log(exam.userCreated.userId);
      console.log(cate);
      this.http
      .post('http://localhost:8080/exam/create', exam)
      .subscribe(success => {
        this.notifierService.notify('success', 'Create exam successfully', '');
        setTimeout(() => { this.router.navigateByUrl('cms/exam'); }, 1000);
      });
    }
  onReset() {
    this.examFrm.reset();
    }
}
