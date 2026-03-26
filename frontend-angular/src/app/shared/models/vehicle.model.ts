export interface Vehicle {
  id: string;
  plate: string;
  brand: string;
  model: string;
  year?: number | null;
  dailyRate: number;
}

