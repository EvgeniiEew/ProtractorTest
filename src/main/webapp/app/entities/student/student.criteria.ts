import * as dayjs from 'dayjs';

export interface IStudentCriteria {
  id?: number;
  firstName?: string;
}

export class StudentCriteria implements IStudentCriteria {
  constructor(public id?: number, public firstName?: string) {}
}
