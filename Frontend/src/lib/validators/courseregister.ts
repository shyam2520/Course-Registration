import { z } from "zod";

export const courseRegisterType = z.object({
  courseCRNS: z.array(z.number().int().positive()),
});

export type CourseRegisterType = z.infer<typeof courseRegisterType>;