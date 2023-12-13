export type course = {
  title: string;
  semester: string;
  hours: number;
  enrollment: number;
  seats: number;
  prerequisite: string[];
  instructor: string;
  classTiming: {
    startTime: string;
    endTime: string;
    days: string;
  }
  crn: number;
}