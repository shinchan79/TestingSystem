import { Component, OnInit } from '@angular/core';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';


import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { mergeMap } from 'rxjs/operators';

//import { Exam } from '../exam.interface';
import { Category } from 'src/app/category/category.interface';
import { Exam } from 'src/app/entity/Exam.interface';


@Component({
  selector: 'app-update-common',
  templateUrl: './update-common.component.html',
  styleUrls: ['./update-common.component.css']
})
export class UpdateCommonComponent implements OnInit {

  examFrm: FormGroup;
  exam: Exam;
  category: Category;
  categories: Category[] = [];
  checkStatus: boolean;
  examid : string;
  inputCategory: string;

  public Editor = ClassicEditor;
  constructor(private activatedRoute: ActivatedRoute, private fb: FormBuilder,
    private http: HttpClient, private router: Router) { }

  ngOnInit() {

    this.examFrm = this.fb.group({
      title: ['', Validators.required],
      category: [''],
      numberOfQuestion: ['', Validators.required],
      duration: ['', Validators.required],
      status: ['',],
      note: ['']
    })

    this.http.get<Category[]>(`http://localhost:8080/category/listCategories`)
      .subscribe(categories => {
        this.categories = categories;
        console.table(this.categories)
      })

    this.activatedRoute.paramMap.pipe(
      mergeMap(params => {
        const examId = params.get('id');
        this.examid =examId;
        console.log(examId);
        return this.http.get<Exam>(`http://localhost:8080/exam/${examId}`);
      })
    ).subscribe(exam => {
      this.exam = exam;
      this.examFrm.patchValue(
        {
          title: exam.title,
          category: exam.category.categoryId,
          numberOfQuestion: exam.numberOfQuestion,
          duration: exam.duration,
          status: exam.status,
          note: exam.note
        }
      );
      console.log("Exam1: " + this.exam.title );
      // console.log("Exam1: " + (this.exam.examQuestions.length == 0?true:false) );
      if(this.exam.examQuestions.length == 0){
        this.checkStatus = true;
      }else{
        this.checkStatus = false;
      }
      console.log("check: " + this.checkStatus);
    })

  }

  onSubmit() {
    const value = this.examFrm.value;
    const categoryId = value.category;
    var category: Category;
    if (categoryId !== '') {
      category = this.categories.find(v => v.categoryId = categoryId);
    } else {
      category = this.exam.category;
    }
    const exam: Exam = {
      ...value
    }
    exam.category = category;

    console.log(exam);

    this.http.put(`http://localhost:8080/exam/update/update-common/${this.exam.examId}`, exam)
      .subscribe(() => {
        this.router.navigateByUrl('cms/exam');
      })
  }

  reset(event) {
    event.preventDefault();
    this.examFrm.patchValue(
      {
        title: this.exam.title,
        category: this.exam.category.categoryId,
        numberOfQuestion: this.exam.numberOfQuestion,
        duration: this.exam.duration,
        status: this.exam.status,
        note: this.exam.note
      }
    );
  }
}

