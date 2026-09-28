const API_URL = "http://localhost:8080";

// ===============================
// LOAD PARKING OCCUPANCY
// ===============================

async function loadOccupancy() {
    try {
        const response = await fetch(
            `${API_URL}/api/slots/occupancy`
        );

        const data = await response.json();

        document.getElementById("totalSlots").textContent =
            data.totalSlots;

        document.getElementById("availableSlots").textContent =
            data.availableSlots;

        document.getElementById("occupiedSlots").textContent =
            data.occupiedSlots;

    } catch (error) {
        console.error("Error loading occupancy:", error);
    }
}


// ===============================
// LOAD PARKING SLOTS
// ===============================

async function loadSlots() {
    try {
        const response = await fetch(
            `${API_URL}/api/slots`
        );

        const slots = await response.json();

        const slotsContainer =
            document.getElementById("slots");

        slotsContainer.innerHTML = "";


        // Fill the booking dropdown
        const slotSelect =
            document.getElementById("slotId");

        slotSelect.innerHTML =
            '<option value="">Select a slot</option>';


        slots
            .filter(slot => slot.available)
            .forEach(slot => {

                const option =
                    document.createElement("option");

                option.value = slot.id;

                option.textContent =
                    `${slot.slotNumber} - Available`;

                slotSelect.appendChild(option);
            });


        // Display all parking slots
        slots.forEach(slot => {

            const slotDiv =
                document.createElement("div");

            slotDiv.classList.add("slot");


            if (slot.available) {

                slotDiv.classList.add("available");

                slotDiv.innerHTML = `
                    <h3>${slot.slotNumber}</h3>
                    <p>Available</p>
                `;

            } else {

                slotDiv.classList.add("occupied");

                slotDiv.innerHTML = `
                    <h3>${slot.slotNumber}</h3>
                    <p>Occupied</p>
                `;
            }


            slotsContainer.appendChild(slotDiv);
        });

    } catch (error) {

        console.error(
            "Error loading slots:",
            error
        );
    }
}


// ===============================
// BOOK PARKING SLOT
// ===============================

async function bookSlot() {

    const vehicleNumber =
        document.getElementById("vehicleNumber").value.trim();

    const slotId =
        document.getElementById("slotId").value;

    const startTime =
        document.getElementById("startTime").value;

    const endTime =
        document.getElementById("endTime").value;

    const message =
        document.getElementById("bookingMessage");


    // Check required fields
    if (
        !vehicleNumber ||
        !slotId ||
        !startTime ||
        !endTime
    ) {

        message.textContent =
            "Please fill all fields.";

        return;
    }


    // Check time
    if (new Date(endTime) <= new Date(startTime)) {

        message.textContent =
            "End time must be after start time.";

        return;
    }


    // Booking data
    const bookingData = {

        slot: {
            id: Number(slotId)
        },

        vehicleNumber: vehicleNumber,

        startTime: startTime,

        endTime: endTime,

        status: "BOOKED"
    };


    try {

        const response = await fetch(
            `${API_URL}/api/bookings`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(bookingData)
            }
        );


        const data =
            await response.json();


        // Backend returned an error
        if (!response.ok) {

            message.textContent =
                data.error || "Booking failed.";

            return;
        }


        // Successful booking
        message.textContent =
            "Booking successful! Booking ID: " + data.id;


        // Clear form
        document.getElementById(
            "vehicleNumber"
        ).value = "";

        document.getElementById(
            "slotId"
        ).value = "";

        document.getElementById(
            "startTime"
        ).value = "";

        document.getElementById(
            "endTime"
        ).value = "";


        // Refresh dashboard
        loadOccupancy();

        loadSlots();


    } catch (error) {

        console.error(
            "Booking error:",
            error
        );

        message.textContent =
            "Could not connect to the backend.";
    }
}


// ===============================
// START APPLICATION
// ===============================

loadOccupancy();

loadSlots();
async function checkIn() {

    const bookingId =
        document.getElementById("checkInBookingId").value;

    const message =
        document.getElementById("checkMessage");

    if (!bookingId) {
        message.textContent = "Enter Booking ID.";
        return;
    }

    try {
        const response = await fetch(
            `${API_URL}/api/check-in-out/check-in/${bookingId}`,
            {
                method: "POST"
            }
        );

        const data = await response.json();

        if (!response.ok) {
            message.textContent =
                data.error || "Check-in failed.";
            return;
        }

        message.textContent =
            "Check-in successful! Check-In ID: " + data.id;

        document.getElementById("checkOutId").value = data.id;

    } catch (error) {
        console.error(error);
        message.textContent = "Could not connect to backend.";
    }
}


async function checkOut() {

    const checkOutId =
        document.getElementById("checkOutId").value;

    const message =
        document.getElementById("checkMessage");

    if (!checkOutId) {
        message.textContent = "Enter Check-In ID.";
        return;
    }

    try {
        const response = await fetch(
            `${API_URL}/api/check-in-out/check-out/${checkOutId}`,
            {
                method: "POST"
            }
        );

        const data = await response.json();

        if (!response.ok) {
            message.textContent =
                data.error || "Check-out failed.";
            return;
        }

        message.textContent =
            "Check-out successful! Penalty: ₹" + data.penalty;

        loadOccupancy();
        loadSlots();

    } catch (error) {
        console.error(error);
        message.textContent = "Could not connect to backend.";
    }
}