import { Component, OnInit } from '@angular/core';
import { ServiceService } from 'src/app/service.service';
import { Question } from 'src/entity/Question';
import { MatTableDataSource } from '@angular/material';
import { SelectionModel } from '@angular/cdk/collections';
import { Level } from 'src/entity/Level';
@Component({
  selector: 'app-list-question',
  templateUrl: './list-question.component.html',
  styleUrls: ['./list-question.component.css']
})
export class ListQuestionComponent implements OnInit {

  listQuestion: Question[];
  listLvl: Level[];
  displayedColumns: string[] = ['select', 'id', 'name', 'action'];
  dataSource = new MatTableDataSource<Question>(this.listQuestion);
  selection = new SelectionModel<Question>(true, []);

  constructor(
    private service: ServiceService
  ) { }

  ngOnInit() {
    this.service.getListQuestion().subscribe(
      lquestion => {
        this.listQuestion = lquestion;
        this.dataSource.data = this.listQuestion;
      }
    );
  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected() ?
      this.selection.clear() :
      this.dataSource.data.forEach(row => this.selection.select(row));
  }

  loadPopupUpdate() {
    this.selection.selected.forEach(element => {
      console.log(element.id)
    });
    console.log(this.selection.selected);
    this.service.getListLvl().subscribe(
      lvl => { this.listLvl = lvl}
    );
  }
}
