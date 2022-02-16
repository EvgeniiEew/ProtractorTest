import * as dayjs from 'dayjs';
import { IProvider } from 'app/entities/provider/provider.model';

export interface ICourses {
  id?: number;
  courseName?: string;
  dateOfStart?: dayjs.Dayjs;
  dateOfEnd?: dayjs.Dayjs;
  name?: IProvider | null;
}

export class Courses implements ICourses {
  constructor(
    public id?: number,
    public courseName?: string,
    public dateOfStart?: dayjs.Dayjs,
    public dateOfEnd?: dayjs.Dayjs,
    public name?: IProvider | null
  ) {}
}

export function getCoursesIdentifier(courses: ICourses): number | undefined {
  return courses.id;
}
