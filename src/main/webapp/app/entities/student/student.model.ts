import * as dayjs from 'dayjs';

export interface IStudent {
  id?: number;
  firstName?: string;
  lastName?: string;
  dateOfBirthday?: dayjs.Dayjs;
}

export class Student implements IStudent {
  constructor(public id?: number, public firstName?: string, public lastName?: string, public dateOfBirthday?: dayjs.Dayjs) {}
}

export function getStudentIdentifier(student: IStudent): number | undefined {
  return student.id;
}
