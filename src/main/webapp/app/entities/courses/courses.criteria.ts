import * as dayjs from 'dayjs';

export interface ICoursesCriteria {
  id?: number;
  courseName?: string;
  dateOfStart?: dayjs.Dayjs;
  dateOfEnd?: dayjs.Dayjs;
}

export class CoursesCriteria implements ICoursesCriteria {
  constructor(public id?: number, public courseName?: string, public dateOfStart?: dayjs.Dayjs, public dateOfEnd?: dayjs.Dayjs) {}
}
