import { course } from "@/types/course";
import { createStore, useStore } from "zustand";
import { createJSONStorage, persist } from "zustand/middleware";

interface CourseStore {
  courses: course[];
  addCourse: (course: course) => void;
  removeCourse: (course: course) => void;
}

const courseStore = createStore<CourseStore>()(
  persist(
    (set) => ({
      courses: [],
      addCourse: (course: course) => set((state) => ({
        courses: [...state.courses, course]
      })),
      removeCourse: (course: course) => set((state) => ({
        courses: state.courses.filter((c) => c.id !== course.id)
      }))
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

//getter
export const getCourses = () => coursesSelector(courseStore.getState());
export const getAddCourse = () => addCourseSelector(courseStore.getState());
export const getRemoveCourse = () => removeCourseSelector(courseStore.getState());

//hooks
export const useCourseStore = () => useStore(courseStore, coursesSelector);
export const useAddCourse = () => useStore(courseStore, addCourseSelector);
export const useRemoveCourse = () => useStore(courseStore, removeCourseSelector);