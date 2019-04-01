import {
  Component,
  OnInit,
  AfterViewInit,
  ViewChild,
  ElementRef,
  Input
} from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import {
  MatTableDataSource,
  MatPaginator,
  MatSort,
  PageEvent
} from '@angular/material';
import { Exam } from 'src/app/entity/Exam.interface';
import { merge } from 'rxjs/observable/merge';
import { mergeMap, debounceTime } from 'rxjs/operators';
import { FormGroup, FormBuilder } from '@angular/forms';
import { v4 as uuid } from 'uuid';
import {
  distinctUntilChanged,
  startWith,
  tap,
  delay,
  map
} from 'rxjs/operators';
import { fromEvent } from 'rxjs/observable/fromEvent';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { MatSortModule } from '@angular/material/sort';
import { Category } from 'src/app/entity/Category.interface';
import { NgModule } from '@angular/core';
import { UploadserviceService } from 'src/app/service/upload/uploadservice.service';
import { NotifierService } from 'angular-notifier';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Role } from 'src/entity/Role';

@Component({
  selector: 'app-list-exam',
  templateUrl: './list-exam.component.html',
  styleUrls: ['./list-exam.component.css']
})
export class ListExamComponent implements OnInit, AfterViewInit {
  selectedFiles: FileList;
  currentFileUpload: File;
  currentFileInport: File;
  mess: string;
  check: boolean;

  public dataSource = new MatTableDataSource<Exam>();
  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  displayedColumns = [
    'examId',
    'title',
    'category',
    'duration',
    'numberOfQuestion',
    'userCreated',
    'status',
    'createAt',

  ];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild('input') input: ElementRef;

  listExam: Exam[] = [];
  listId: string[] = [];
  listDuration: number[] = [];
  numberOfQuestions: number[] = [];
  statuss: String[] = [];
  caterogyNames: String[] = [];
  roless: Role[];
  isCheckALL = false;
  examFrm: FormGroup;
  private roles: string[];
  private authority: string;
  showFile = false
  fileUploads: Observable<string[]>

  pageEvent: PageEvent;
  searchTerm: string;

  public duration: number;
  public numberOfQuestion: number;
  public createAt: Date = new Date('dd/mm/yyyy');
  public status: String;
  public category: Category;
  public categoryName: String;

  constructor(private fb: FormBuilder,
    private uploadService: UploadserviceService,
    private http: HttpClient,
    private tokenStorage: TokenStorageService,
     private notifierService: NotifierService) {

  }
  ngOnInit() {
    this.dataRole();
    if (this.tokenStorage.getToken()) {
      this.roles = this.tokenStorage.getAuthorities();
      this.roles.every(role => {
        if (role === 'ROLE_ADMIN') {
          this.authority = 'admin';
          return false;
        }
        return true;
      });
    }

    this.findExams('title', 'ASC', '');
    this.examFrm = this.fb.group({
      duration: [''],
      numberOfQuestion: [''],
      createAt: [''],
      status: [''],
      categoryName: [''],
      roleName: ['']
    });
    this.getDuration();
  }

  ngAfterViewInit() {
    fromEvent(this.input.nativeElement, 'keyup')
      .pipe(
        debounceTime(150),
        distinctUntilChanged(),
        tap(() => {
          this.paginator.pageIndex = 0;
          this.loadExamsPage();
        })
      )
      .subscribe();

    // reset the paginator after sorting
    this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(tap(() => this.loadExamsPage()))
      .subscribe();
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }
  public getSearch(e) {
    this.searchTerm = e.target.value;
    console.log(this.searchTerm);
  }
  // This function is to find Exams from the API backend
  public findExams = (
   // pageNumber = 0,
   // pageSize = 5,
    sortTerm = 'title',
    sortOrder = 'ASC',
    searchContent = ''
  ) => {
    this.http
      .get<Exam[]>('http://localhost:8080/exam/listExams/pagination', {
        params: new HttpParams()
         // .set('pageNumber', pageNumber.toString())
         // .set('pageSize', pageSize.toString())
          .set('sortTerm', sortTerm)
          .set('sortOrder', sortOrder)
          .set('searchContent', searchContent)
      })
      .subscribe(listExam => {
        this.listExam = listExam;
        this.dataSource.data = listExam;
      });
  }

  public loadExamsPage() {
    this.findExams(
      // this.paginator.pageIndex,
      // this.paginator.pageSize,
      this.sort.active,
      this.sort.direction,
      this.input.nativeElement.value
    );
  }

  public doFilter = (value: string) => {

    this.paginator.pageIndex = 0;
    this.findExams(
      // this.paginator.pageIndex,
      // this.paginator.pageSize,
      this.sort.active,
      this.sort.direction,
      value
    );
  }

  // Start Delete
  onCheck(event) {
    const input = event.target as HTMLInputElement;
    this.isCheckALL = input.checked;
  }
  onchange(event, examId) {
    const checkId = event.target.checked;
    if (checkId) {
      this.listId.push(examId);
    } else {
      const x = this.listId.findIndex(x => {
        return x === examId;
      });
      if (x !== -1) {
        this.listId.splice(x, 1);
      }
    }
  }
  onCheckAllId(event) {
    const checkId = event.target.checked;
    if (checkId) {
      if (this.listId.length > 0) {
        this.listId = [];
      }
      this.listExam.forEach(x => {
        this.listId.push(x.examId);
      });
    } else {
      this.listId = [];
    }
  }

  deleteAllExam() {
      if (this.listId.length > 0) {
        this.listId.forEach(element => {
          this.http
            .delete(`http://localhost:8080/exam/${element}`)
            .pipe(
              mergeMap(() =>
                this.http.get<Exam[]>(
                  'http://localhost:8080/exam/listExams/pagination'
                )
              )
            )
            .subscribe(listExam => {
              this.listExam = listExam;
              this.dataSource.data = listExam;
            });
        });
      }
  }
  // end

  // Filter Start
  onSubmit() {
    console.log(this.examFrm.value);
    if (this.examFrm.valid) {
      const value = this.examFrm.value;
      const exam: Exam = {
        id: uuid(),
        ...value
      };
      this.http
        .post<Exam[]>('http://localhost:8080/exam/filter', exam)
        .subscribe(listExam => {
          (this.listExam = listExam), console.log(this.listExam);
          this.dataSource.data = listExam;
        });
    }
  }
  getDuration() {
    this.http
      .get('http://localhost:8080/exam/listExams')
      .subscribe((exams: Exam[]) => {
        exams.forEach(x => {
          this.listDuration.push(x.duration);
          this.numberOfQuestions.push(x.numberOfQuestion);
          this.statuss.push(x.status);
          this.caterogyNames.push(x.category.categoryName);
        });
        (this.listDuration = this.listDuration.filter(function (
          item,
          index,
          self
        ) {
          return index === self.indexOf(item);
        })),
          (this.numberOfQuestions = this.numberOfQuestions.filter(function (
            item,
            index,
            self
          ) {
            return index === self.indexOf(item);
          })),
          (this.statuss = this.statuss.filter(function (item, index, self) {
            return index === self.indexOf(item);
          })),
          (this.caterogyNames = this.caterogyNames.filter(function (
            item,
            index,
            self
          ) {
            return index === self.indexOf(item);
          }));
      });
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  upload() {
    this.currentFileUpload = this.selectedFiles.item(0);
    this.uploadService.pushFileToStorage(this.currentFileUpload).subscribe(
      success => {
        console.log("upload succesfully!")
      },
      error => {
        console.log("error: " + error.error.text);
        if (error.error.text === 'Not matching extension file') {
          this.notifierService.notify('error', 'Not matching extension file');
        }else if (error.error.text === 'OK') {
          this.notifierService.notify('success', 'successfully uploaded ' + this.currentFileUpload.name + '!');
          this.showFiles(false);
          this.uploadService.importToServer(this.currentFileUpload)
            .subscribe(
              success => {
              },
              error => {
                console.log("error: " + error.error.text);
                if (error.error.text === 'Ok') {
                  this.notifierService.notify('success', 'Import exam successfully');
                  this.loadExamsPage();
                  //window.location.reload();
                  //this.router.navigate(['']);
                } else if (error.error.text === 'not Ok') {
                  this.notifierService.notify('error', 'Import exam Failed');
                }else if(error.error.text === 'this excel file is empty'){
                  this.notifierService.notify('warning', 'this excel file is empty');
                }else if(error.error.text === "this excel file is not formatted with one's template"){
                  this.notifierService.notify('warning', "this excel file is not formatted with one's template");
                }
              }
            );
        } else if (error.error.text === ("ERROR! can't upload " + this.selectFile + "!")) {
          this.notifierService.notify('error', '"ERROR! cant upload "' + this.selectFile + "!");
        }
      }
    )
    //end
  }
  dataRole() {
    this.http
      .get(`http://localhost:8080/role/list`)
      .subscribe((ob: Role[]) => {
        this.roless = ob;
        console.log(this.roles);
      });
  }
  showFiles(enable: boolean) {
    this.showFile = enable
    if (enable) {
      this.fileUploads = this.uploadService.getFiles();
    }
  }
}
