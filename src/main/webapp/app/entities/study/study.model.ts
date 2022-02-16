import * as dayjs from 'dayjs';
import { ICourses } from 'app/entities/courses/courses.model';
import { IStudent } from 'app/entities/student/student.model';

export interface IStudy {
  id?: number;
  dateOfStart?: dayjs.Dayjs;
  dateOfExam?: dayjs.Dayjs;
  grade?: number;
  coursename?: ICourses | null;
  student?: IStudent | null;
}

export class Study implements IStudy {
  constructor(
    public id?: number,
    public dateOfStart?: dayjs.Dayjs,
    public dateOfExam?: dayjs.Dayjs,
    public grade?: number,
    public coursename?: ICourses | null,
    public student?: IStudent | null
  ) {}
}

export function getStudyIdentifier(study: IStudy): number | undefined {
  return study.id;
}
