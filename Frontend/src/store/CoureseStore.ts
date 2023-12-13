
import { courseTable } from "@/types/courseTable";
import { createStore, useStore } from "zustand";
import { createJSONStorage, persist } from "zustand/middleware";

interface CourseStore {
  courses: courseTable[];
  addCourse: (course: courseTable) => void;
  removeCourse: (crn: number) => void;
  reset: () => void;
}

const courseStore = createStore<CourseStore>()(
  persist(
    (set) => ({
      courses: [],
      addCourse: (course: courseTable) => set((state) => ({
        courses: [...state.courses, course]
      })),
      removeCourse: (crn: number) => set((state) => ({
        courses: state.courses.filter((c) => c.crn !== crn)
      })),
      reset: () => set(() => ({ courses: [] })),
    }),
    {
      name: "course-storage",
      storage: createJSONStorage(() => sessionStorage),
    }
  )
)

//selector
const coursesSelector = (state: CourseStore) => state.courses;
const addCourseSelector = (state: CourseStore) => state.addCourse;
const removeCourseSelector = (state: CourseStore) => state.removeCourse;
const resetSelector = (state: CourseStore) => state.reset;

//getter
export const getCourses = () => coursesSelector(courseStore.getState());
export const getAddCourse = () => addCourseSelector(courseStore.getState());
export const getRemoveCourse = () => removeCourseSelector(courseStore.getState());
export const getReset = () => resetSelector(courseStore.getState());

//hooks
export const useCourse = () => useStore(courseStore, coursesSelector);
export const useAddCourse = () => useStore(courseStore, addCourseSelector);
export const useRemoveCourse = () => useStore(courseStore, removeCourseSelector);
export const useReset = () => useStore(courseStore, resetSelector);