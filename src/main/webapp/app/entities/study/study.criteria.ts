import * as dayjs from 'dayjs';
import { ICourses } from 'app/entities/courses/courses.model';
import { IStudent } from 'app/entities/student/student.model';

export interface IStudyCriteria {
  id?: number;
  dateOfStart?: dayjs.Dayjs;
  coursesCourseName?: string;
  studentFirstName?: string;
}

export class StudyCriteria implements IStudyCriteria {
  constructor(public id?: number, public dateOfStart?: dayjs.Dayjs, public coursesCourseName?: string, public studentFirstName?: string) {}
}
