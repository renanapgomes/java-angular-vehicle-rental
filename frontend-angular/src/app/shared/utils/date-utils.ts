// Dias inclusivos entre datas no formato yyyy-MM-dd.
export function countInclusiveDaysIso(startIso: string, endIso: string): number {
  if (!startIso || !endIso) return 0;
  const start = new Date(`${startIso}T00:00:00`);
  const end = new Date(`${endIso}T00:00:00`);
  if (Number.isNaN(start.getTime()) || Number.isNaN(end.getTime())) return 0;
  if (end < start) return 0;
  const msPerDay = 86_400_000;
  return Math.floor((end.getTime() - start.getTime()) / msPerDay) + 1;
}

