
export type course = {
  id: string;
  title: string;
  crn: number;
  semester: string;
  hours: number;
  enrolled: number;
  seats: number;
  prerequisites: string[];
  instructor: string;
  time: string;
};
