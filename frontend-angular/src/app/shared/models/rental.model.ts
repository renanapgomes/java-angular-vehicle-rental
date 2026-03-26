export type RentalStatus = 'ACTIVE' | 'CANCELLED';

export interface Rental {
  id: string;
  vehicleId: string;
  vehiclePlate?: string | null;
  clientId: string;
  clientName?: string | null;
  startDate: string;
  endDate: string;
  totalAmount: number;
  status: RentalStatus;
}

